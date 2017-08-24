from .. import Base
from sqlalchemy import Column, Integer, String

class POI(Base):
	__tablename__ = 'poi'

	id = Column(Integer, primary_key=True)
	building_id = Column(Integer)
	name = Column(String(256))
	url = Column(String(512))
	x = Column(Integer)
	y = Column(Integer)

	def __init__(self, building_id, name, url, x, y):
		self.building_id = building_id
		self.name = name
		self.url = url
		self.x = x
		self.y = y

	@property
	def serialize(self):
		return {
			'id': self.id,
			'building_id': self.building_id,
			'name': self.name,
			'url': self.url,
			'x': self.x,
			'y': self.y
		}